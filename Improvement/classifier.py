from sklearn.naive_bayes import GaussianNB
from sklearn.metrics import accuracy_score
import numpy as np

def dataset2Array(filename):
    arr = []
    with open(filename) as text:
        for line in text:
            arr.append(map(int, line.split(',')))

    return arr

def createLabels(n):
    arr1 = [1] * n
    arr2 = [2] * n
    arr3 = [3] * n
    arr4 = [4] * n
    return arr1 + arr2 + arr3 + arr4

def classify(train_file, test_file):
    clf = GaussianNB()

    features_train = dataset2Array(train_file)
    labels_train = createLabels(250)
    clf.fit(features_train, labels_train)

    features_test = dataset2Array(test_file)
    pred = clf.predict(features_test)
    labels_test = createLabels(50)

    accuracy = accuracy_score(labels_test, pred)

    target_names = ['politics', 'religion', 'science', 'sports']

    print accuracy
    from sklearn.metrics import classification_report

    print classification_report(pred, labels_test, target_names=target_names)

if ( __name__ == "__main__"):
    train_file = "ml_dataset_train.txt"
    test_file = "ml_dataset_test.txt"
    from datetime import datetime
    print(str(datetime.now()))
    classify(train_file, test_file)
    print(str(datetime.now()))