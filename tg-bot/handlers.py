from aiogram import Router, F, Bot
from aiogram.filters import CommandStart, Command
from aiogram.types import Message
from msg_formatter import format_tasks
import logging

from task_management_api_client import TaskManagementApiClient
logger = logging.getLogger(__name__)

router = Router()

@router.message(CommandStart())
async def cmd_start(message: Message):
    await message.answer('Привет! Рад тебя видеть :)')

@router.message(Command('tasks'))
async def cmd_tasks(message: Message, api_client: TaskManagementApiClient):
    tasks = await api_client.get_tasks()
    await message.answer(f"Ваши задачи: {format_tasks(tasks)}")

@router.message(F.text == 'привет')
async def cmd_hello(message: Message):
    await message.answer('Запуск сообщения по команде /start_3 используя магический фильтр F.text!')
