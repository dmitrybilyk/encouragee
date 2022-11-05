from python:3.8

WORKDIR /code

COPY ./requirements.txt .

RUN pip install -r ./requirements.txt

COPY ./script.py .

CMD ["python", "./script.py"]