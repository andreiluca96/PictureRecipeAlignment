import keras
import tensorflow
import numpy as np
import main


def create_model():
    keras.backend.clear_session()
    global graph
    graph = tensorflow.get_default_graph()
    init_op = tensorflow.global_variables_initializer()
    config = tensorflow.ConfigProto()
    config.gpu_options.allow_growth = True
    session = tensorflow.Session(graph=graph, config=config)

    if 'session' in locals() and session is not None:
        print('Close interactive session')
        session.close()
    mod = main.setup_model()
    # model.load_weights('model3.h5')
    # mod.load_weights('C:/Users/Bogdan/Downloads/model3matco.h5')
    mod._make_predict_function()
    return mod


def train_batch(starting_point, model, batch_size=5):
    batch_train_data = main.train_data[starting_point:starting_point+batch_size]
    model.fit(
        [np.array(list(map(lambda x: x['ingredients'], batch_train_data))),
         np.array(list(map(lambda x: main.preprocess(x['image'])[0], batch_train_data)))],
        np.zeros((len(batch_train_data))), epochs=10)
    model.save('batch-model'+str(starting_point)+'.h5')


if __name__ == '__main__':
    main.setup_train_data()
    model = create_model()
    print(len(main.train_data))
    for starting_point in range(0, len(main.train_data), 5):
        print(starting_point)
        train_batch(starting_point, model)

