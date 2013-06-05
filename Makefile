JC = javac
cp="./classes:libs/beaver-cc-0.9.11.jar"
classdir=classes


default:
		java -jar libs/JFlex.jar src/cool/mycool.jflex
		java -jar libs/beaver-cc-0.9.11.jar	-w -a -T -n -d src  src/cool/cool.grammar
		
		$(JC) -d $(classdir) -cp $(cp) src/cool/parser/ast/JSONLogger.java 

		$(JC) -d $(classdir) -cp $(cp) src/cool/parser/ast/Node.java 
		$(JC) -d $(classdir) -cp $(cp) src/cool/parser/ast/Actuals.java 

		$(JC) -d $(classdir) -cp $(cp) src/cool/parser/ast/Expr.java 
		$(JC) -d $(classdir) -cp $(cp) src/cool/parser/ast/BExpr.java 
		
		$(JC) -d $(classdir) -cp $(cp) src/cool/parser/ast/RExpr.java 
		$(JC) -d $(classdir) -cp $(cp) src/cool/parser/ast/Block.java 
		
		$(JC) -d $(classdir) -cp $(cp) src/cool/parser/ast/Formal.java 
		$(JC) -d $(classdir) -cp $(cp) src/cool/parser/ast/Var.java 
		$(JC) -d $(classdir) -cp $(cp) src/cool/parser/ast/VarExpr.java 
		$(JC) -d $(classdir) -cp $(cp) src/cool/parser/ast/Feature.java 
		$(JC) -d $(classdir) -cp $(cp) src/cool/parser/ast/FeatureBlock.java 
		$(JC) -d $(classdir) -cp $(cp) src/cool/parser/ast/FeatureMethod.java 
		$(JC) -d $(classdir) -cp $(cp) src/cool/parser/ast/FeatureVar.java 


		$(JC) -d $(classdir) -cp $(cp) src/cool/parser/ast/Extends.java 


		$(JC) -d $(classdir) -cp $(cp) src/cool/parser/ast/Primary.java 

		$(JC) -d $(classdir) -cp $(cp) src/cool/parser/ast/IfNode.java 
		$(JC) -d $(classdir) -cp $(cp) src/cool/parser/ast/IntegerNode.java 

		$(JC) -d $(classdir) -cp $(cp) src/cool/parser/ast/MyInt.java 
		$(JC) -d $(classdir) -cp $(cp) src/cool/parser/ast/Node.java 
		$(JC) -d $(classdir) -cp $(cp) src/cool/parser/ast/NullNode.java 
		$(JC) -d $(classdir) -cp $(cp) src/cool/parser/ast/ObjectNode.java 


		$(JC) -d $(classdir) -cp $(cp) src/cool/parser/ast/Id.java 
		$(JC) -d $(classdir) -cp $(cp) src/cool/parser/ast/StringNode.java 



		$(JC) -d $(classdir) -cp $(cp) src/cool/parser/ast/WhileNode.java 
		
		$(JC) -d $(classdir) -cp $(cp) src/cool/parser/ast/ClassNode.java 
		$(JC) -d $(classdir) -cp $(cp) src/cool/parser/Terminals.java 
		$(JC) -d $(classdir) -cp $(cp) src/cool/MyCoolError.java 
		$(JC) -d $(classdir) -cp $(cp) src/cool/MyCoolScanner.java 
		$(JC) -d $(classdir) -cp $(cp) src/cool/parser/SimpleParser.java 
		$(JC) -d $(classdir) -cp $(cp) src/cool/MyCoolParser.java 
		$(JC) -d $(classdir) -cp $(cp) src/MyCoolMain.java 


classes: $(CLASSES:.java=.class)
	
clean:
	$(RM) *.class




