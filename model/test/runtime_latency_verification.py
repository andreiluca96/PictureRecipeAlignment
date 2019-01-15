import time
import requests

if __name__ == "__main__":
    data = dict()
    data['ingredients'] = ['butter']
    start_time = time.time() * 1000.0
    requests.post("http://localhost:5000/ingredients", json=data)
    stop_time = time.time() * 1000.0
    print("Ingredient request took", int(stop_time-start_time), "milliseconds")