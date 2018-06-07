FROM pklehre/niso-lab4

ADD ArgParser.java /bin
ADD bas782.java /bin
ADD DataHolder.java /bin
ADD ExpressionGenerator.java /bin
ADD RandomWrapper.java /bin
ADD GeneticAlgorithm.java /bin
ADD ExpressionNode.java /bin
ADD jsexp-0.2.2.jar /bin

RUN javac -cp ./bin/jsexp-0.2.2.jar: -d ./bin/. ./bin/*.java

CMD ["-username", "bas782", "-submission" , "java -cp ./bin:./bin/jsexp-0.2.2.jar: bas782"]