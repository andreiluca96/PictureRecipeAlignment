import threading
import time
import requests
from multiprocessing import Process

def f(thr):
    data = dict()
    data['ingredients'] = ['butter']
    print("sending" + str(thr))
    requests.post("http://localhost:5000/ingredients", json=data)
    print("receiving" + str(thr))

if __name__ == '__main__':
    thread_list = []

    for i in range(1, 100):
        t = threading.Thread(target=f, args=(i,))
        thread_list.append(t)

    for thread in thread_list:
        thread.start()

    for thread in thread_list:
        thread.join()
