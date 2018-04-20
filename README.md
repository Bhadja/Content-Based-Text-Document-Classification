Pre-Requisite:
scikit learn python package
practicalnlptools python package
stanford parser jars

BaseLine:
javac BaseLine.java
java BaseLine | tee output.txt

This will fetch all the training files, count the frequency of words in each file, form the bag of words for each class and then calculate the class for each document.

Improvement:
prepareVector.py
	The script takes the nlp_train_set.txt (concatenated train files) and outputs the machine learning equivalent file ml_dataset_train.txt
prepareTestData.py
	The script takes the nlp_test_set.txt (concatenated test files) and outputs the machine learning equivalent file ml_dataset_test.txt
classifier.py
	It takes the ml_dataset_train.txt and ml_dataset_test.txt and creates the machine learning model.
