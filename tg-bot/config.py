import logging, os
from dotenv import load_dotenv

logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(name)s - %(levelname)s - %(message)s')
logger = logging.getLogger(__name__)

load_dotenv()

KAFKA_HOST = os.getenv("KAFKA_BOOTSTRAP_HOST", "localhost")
KAFKA_PORT = os.getenv("KAFKA_BOOTSTRAP_PORT", "9092")
KAFKA_TOPIC = os.getenv("KAFKA_TOPIC", "telegram")
KAFKA_GROUP_ID = os.getenv("KAFKA_GROUP_ID", "tg-bot-group")
API_TOKEN = os.getenv("TG_BOT_TOKEN", None)
