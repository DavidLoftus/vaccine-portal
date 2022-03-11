FROM python:3.6-slim

WORKDIR /root

COPY requirements.txt .
RUN pip install -r requirements.txt

COPY init.py .

CMD python init.py db