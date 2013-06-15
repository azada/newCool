JC = javac -sourcepath src/
cp="./classes:libs/beaver-cc-0.9.11.jar"
classdir=classes
AST=src/cool/parser/ast
EXP=src/cool/exception
SYM=src/cool/symbol
PAS=src/cool/parser
GEN=src/cool/codegen

default:
		

		java -jar libs/JFlex.jar src/cool/mycool.jflex
		java -jar libs/beaver-cc-0.9.11.jar	-w -a -T -n -t -d src  src/cool/cool.grammar
		$(JC) -d $(classdir) -cp $(cp) **/*.java
		
		
		


classes: $(CLASSES:.java=.class)
	
clean:
	$(RM) *.class




