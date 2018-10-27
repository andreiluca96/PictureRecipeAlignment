import argparse
import json
import os
import sys
import urllib.request
from multiprocessing import Process

NO_IMAGES = 887536


def download_image(uri, filename):
    urllib.request.urlretrieve(uri, filename)


def download_batch(index, start, end, path, directory):
    i = start

    with open(path) as f:
        data = json.load(f)
        for dish in data[start:end]:
            for image in dish['images']:
                i += 1
                try:
                    download_image(image['url'], os.path.join(directory, image['id']))
                except:
                    pass
                if i % 10 == 0:
                    print("Process %s: downloaded %f%%, reached %d" % (index, 100 * (i - start) / (end - start), i))


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description='Downloads pictures from the dataset locally in parallel')
    parser.add_argument('-np', '--no-processes', type=int, help='No of processes to spawn and work in parallel',
                        required=True)
    parser.add_argument('-lp', '--layer2-path', help='layer2.json file path', required=True)
    parser.add_argument('-od', '--output-directory', help='The directory where the pictures will be downloaded',
                        required=True)

    parse_result = parser.parse_args(sys.argv[1:])

    if not os.path.exists(parse_result.output_directory):
        os.makedirs(parse_result.output_directory)

    batch_size = NO_IMAGES // parse_result.no_processes
    processes = [Process(target=download_batch,
                         args=(
                             index, start, start + batch_size, parse_result.layer2_path, parse_result.output_directory))
                 for (index, start) in enumerate(range(0, NO_IMAGES, batch_size))]

    for p in processes:
        p.start()
    for p in processes:
        p.join()
