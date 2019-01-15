from pythonrv import rv
import main
import endpoint


@rv.spec(when=rv.POST)
@rv.monitor(predict=endpoint.predict_image)
def predict_image_should_have_an_input(event):
    assert event.fn.fact.inputs[0] >= 0, "Predict image function should be given an image as input"
    print("Result of monitor: predict_image_should_have_an_input is TRUE")


@rv.monitor(predict=endpoint.predict_ingredients)
def predict_ingredients_should_have_a_list_as_input(event):
    assert isinstance(event.fn.fact.inputs[0], list), "Predict ingredients function should be given a list of ingredients as input"
    print("Result of monitor: predict_ingredients_should_have_a_list_as_input is TRUE")

@rv.spec(when=rv.POST, history_size=10)
@rv.monitor(preprocess=endpoint.preprocess_image, predict=endpoint.predict_image)
def preprocess_then_predict_spec(event):
    if event.fn.predict.called:
        count_preprocess=0
        count_predict=0
        for old_event in event.history:
            if old_event.called_function == old_event.fn.preprocess:
                count_preprocess+=1
            if old_event.called_function == old_event.fn.predict_image:
                count_predict+=1
        assert count_predict > count_preprocess, "Prediction without proprocess"
        print("Result of monitor: preprocess_then_predict is TRUE")


@rv.spec(when=rv.POST, history_size=10)
@rv.monitor(data=main.setup_train_data, model=main.setup_model())
def after_training_load_model_must_be_constructed(event):
    if event.called_function == event.fn.data:
        assert event.next_called_should_be(event.fn.model) >= 0, "After loading data, load model should be called"
        print("Result of monitor: after_training_load_model_must_be_constructed is TRUE")
