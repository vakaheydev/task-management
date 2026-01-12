import asyncio
from aiogram import Bot, Dispatcher
from aiogram.client.default import DefaultBotProperties
from config import API_TOKEN, KAFKA_HOST, KAFKA_PORT, KAFKA_TOPIC, KAFKA_GROUP_ID, CLIENT_USERNAME, CLIENT_PASSWORD, BASE_URL
from handlers import router
from aiogram.enums import ParseMode
from aiogram.fsm.storage.memory import MemoryStorage
from kafka_consumer import consume_notifications
from task_management_api_client import TaskManagementApiClient

bot = Bot(token=API_TOKEN, default=DefaultBotProperties(parse_mode=ParseMode.HTML))
dp = Dispatcher(storage=MemoryStorage())

async def send_message(user_id: int, text: str):
    try:
        await bot.send_message(chat_id=user_id, text=text)
    except Exception as e:
        print(f"Failed to send message to {user_id}: {e}")

async def main():
    dp = Dispatcher()
    dp.include_router(router)
    
    # Создаем один экземпляр API клиента на всё приложение
    async with TaskManagementApiClient() as api_client:
        # Сохраняем клиент в workflow_data диспетчера
        dp.workflow_data.update({"api_client": api_client})
        
        await bot.delete_webhook(drop_pending_updates=True)

        tasks = [
            dp.start_polling(bot),
            consume_notifications(KAFKA_HOST, KAFKA_PORT, KAFKA_TOPIC, KAFKA_GROUP_ID)
        ]
        await asyncio.gather(*tasks)

if __name__ == "__main__":
    asyncio.run(main())
