import prepareVector as pv

def prepareFeature(feature_file):
    with open(feature_file) as text:
        for line in text:
            return line.split(",")

def prepareVector(feature_list, in_fileName, out_fileName):
    import os.path
    if os.path.isfile(out_fileName):
        print("Output File already present")
        return

    f = open(out_fileName, 'w')

    with open(in_fileName) as text:
        count = 0
        for line in text:
            result = ""
            print("\n"+line)
            vector = pv.vectorize(line)

            flag = True
            for feature in feature_list:
                if(flag):
                    flag = False
                    if feature in vector:
                        result += str(vector[feature])
                    else:
                        result += "0"
                else:
                    if feature in vector:
                        result += "," + str(vector[feature])
                    else:
                        result += "," + "0"

            f.write(result+"\n")
            count += 1

        f.close()

if ( __name__ == "__main__"):
    in_fileName = "nlp_test_set.txt"
    out_fileName = "ml_dataset_test.txt"
    feature_file = "ml_class_names.txt"
    feature_list = prepareFeature(feature_file)
    prepareVector(feature_list, in_fileName, out_fileName)