FROM python:3.12-slim

WORKDIR /app

COPY ./tg-bot/requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

COPY ./tg-bot/. .

CMD ["python", "bot.py"]
