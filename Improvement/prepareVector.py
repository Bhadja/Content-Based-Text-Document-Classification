from nltk.corpus import stopwords
import datetime

def vectorize(line):
    from nltk.corpus import wordnet as wn
    import nltk

    vector = {}

    result = []
    from practnlptools.tools import Annotator
    annotator = Annotator()

    result = annotator.getAnnotations(line)['srl']

    print_res = {}
    for dict in result:
        for key in dict:
            if key not in vector:
                vector[key] = 1
            else:
                vector[key] += 1

            if key not in print_res:
                print_res[key] = 1
            else:
                print_res[key] += 1

    print "SEMANTIC FEATUREs:"
    print "Semantic Role Labelling"
    print print_res

    arr = line.split();

    # HYPERNYM
    print_res = {}
    for word in arr:
        if len(wn.synsets(word)) > 0:
            for ss in wn.synsets(word)[0].hypernyms():
                if len(ss.hypernyms()) > 0:
                    # result.append( (word, ss.hypernyms()[0].name().split(".")[0]) )
                    val = ss.hypernyms()[0].name().split(".")[0]
                    if val.lower() not in vector:
                        vector[val.lower()] = 1
                    else:
                        vector[val.lower()] += 1

                    if val.lower() not in print_res:
                        print_res[val.lower()] = 1
                    else:
                        print_res[val.lower()] += 1

    print "Hypernym"
    print print_res

    # print("hypernym ", datetime.datetime.now( )

    #POS TAGGING
    result =  nltk.pos_tag(arr)
    print_res = {}

    for obj in result:
        if obj[1].lower() not in vector:
            vector[obj[1].lower()] = 1
        else:
            vector[obj[1].lower()] += 1

        if obj[1].lower() not in print_res:
            print_res[obj[1].lower()] = 1
        else:
            print_res[obj[1].lower()] += 1

    #print("pos tagging ",datetime.datetime.now())
    print "\nSYNTACTIC FEATURES:"
    print "POS Tagger"
    print print_res

    # DEPENDENCY PARSING
    print_res = {}
    from nltk.parse.stanford import StanfordDependencyParser
    path_to_jar = 'stanford-parser-full-2015-12-09/stanford-parser.jar'
    path_to_models_jar = 'stanford-parser-full-2015-12-09/stanford-parser-3.6.0-models.jar'
    dependency_parser = StanfordDependencyParser(path_to_jar=path_to_jar, path_to_models_jar=path_to_models_jar)

    dp = dependency_parser.raw_parse(line)
    dep = dp.next()

    for obj in list(dep.triples()):
        if obj[1].lower() not in vector:
            vector[obj[1].lower()] = 1
        else:
            vector[obj[1].lower()] += 1

        if obj[1].lower() not in print_res:
            print_res[obj[1].lower()] = 1
        else:
            print_res[obj[1].lower()] += 1

    print "Dependency Parser"
    print print_res

    #print("DP ", datetime.datetime.now())

    # STEMMING
    print_res = {}
    from nltk.stem.porter import PorterStemmer
    porter_stemmer = PorterStemmer()
    for word in arr:
        val = porter_stemmer.stem(word)
        if val.lower() not in vector:
            vector[val.lower()] = 1
        else:
            vector[val.lower()] += 1

        if val.lower() not in print_res:
            print_res[val.lower()] = 1
        else:
            print_res[val.lower()] += 1

    print "\nSYNTACTIC FEATURES:"
    print "Stemmer"
    print print_res

    #print result

    #print("stemming ", datetime.datetime.now())

    # LEMATIZING
    print_res = {}
    from nltk.stem import WordNetLemmatizer
    wordnet_lemmatizer = WordNetLemmatizer()
    for word in arr:
        val = wordnet_lemmatizer.lemmatize(word)
        if val.lower() not in vector:
            vector[val.lower()] = 1
        else:
            vector[val.lower()] += 1

        if val.lower() not in print_res:
            print_res[val.lower()] = 1
        else:
            print_res[val.lower()] += 1

    print "Lemmetizer"
    print print_res
    return vector

def prepareVector(in_filename, out1_filename, out2_filename):
    count = 0
    with open(in_filename) as text:
        main_list = []
        main_vector = set()
        for line in text:
            print("\n"+line)
            vector = vectorize(line)

            for key in vector.iterkeys():
               main_vector.add(key)

            main_list.append(vector)
            #print(count, " --- ", len(main_vector), "--", len(main_list))
            count += 1
            #if count==2:
            #    break
        #print(len(main_list))



    #main_list = num of lines
    #main_Vector = feature space

    f = open(out1_filename, 'w')
    result = ""
    flag = True
    for feature in main_vector:
        if (flag):
            flag = False
            result += feature
        else:
            result += "," + feature
    f.write(result+"\n")

    f = open(out2_filename, 'w')

    for vector in main_list:
        result = ""
        flag = True
        for feature in main_vector:
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

    f.close()

if ( __name__ == "__main__"):
    in_fileName = "nlp_train_set.txt"
    out1_fileName = "ml_class_names.txt"
    out2_fileName = "ml_dataset_train.txt"

    import os.path
    if os.path.isfile(out2_fileName):
        print("Output File already present")
    else:
        prepareVector(in_fileName, out1_fileName, out2_fileName)