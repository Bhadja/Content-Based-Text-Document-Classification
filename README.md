# Content Based Text Document Classification using Natural Language Processing. 
Pre-Requisite:<br />
scikit learn python package<br />
practnlptools python package<br />
stanford parser jars<br />

BaseLine:<br />
javac BaseLine.java<br />
java BaseLine | tee output.txt<br />

This will fetch all the training files, count the frequency of words in each file, form the bag of words for each class and then calculate the class for each document.<br />

Improvement:<br />
prepareVector.py<br />

	The script takes the nlp_train_set.txt (concatenated train files) and outputs the machine learning equivalent file ml_dataset_train.txt.

prepareTestData.py<br />

	The script takes the nlp_test_set.txt (concatenated test files) and outputs the machine learning equivalent file ml_dataset_test.txt

classifier.py<br />

	It takes the ml_dataset_train.txt and ml_dataset_test.txt and creates the machine learning model.
