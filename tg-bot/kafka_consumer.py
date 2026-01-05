import json
from aiokafka import AIOKafkaConsumer
from dotenv import load_dotenv

load_dotenv()

# =========================
# Async Kafka consumer
# =========================
async def consume_notifications(kafka_host, kafka_port, kafka_topic, kafka_group_id):
    from bot import send_message
    
    consumer = AIOKafkaConsumer(
        kafka_topic,
        bootstrap_servers=f"{kafka_host}:{kafka_port}",
        group_id=kafka_group_id,
        value_deserializer=lambda m: json.loads(m.decode("utf-8"))
    )
    
    await consumer.start()
    print(f"Kafka consumer started on topic '{kafka_topic}'")
    try:
        async for msg in consumer:
            data = msg.value
            print(f"Received message: {data}")
            user_id = data['chanelUserId'] 
            text = data['message']
            if user_id and text:
                await send_message(user_id, text)
    finally:
        await consumer.stop()


if __name__ == "__main__":
    print('This module is intended to be imported, not run directly.')
