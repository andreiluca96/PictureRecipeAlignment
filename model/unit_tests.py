import base64
import unittest

import main
import endpoint


class TestMain(unittest.TestCase):

    def test_setup_train_data(self):
        main.setup_train_data()

        self.assertIsNotNone(main.train_data)
        self.assertTrue(len(main.train_data) > 0)

    def test_setup_model(self):
        model = main.setup_model()

        self.assertIsNotNone(model)

    def test_classify_ingredients(self):
        main.setup_train_data()
        endpoint.create_model()
        test_ingredients = [list(main.all_ingredients)[0]]

        classification = endpoint.predict_ingredients(test_ingredients)

        self.assertTrue(len(list(base64.b64decode(classification))) > 0)
