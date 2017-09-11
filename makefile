all: Engine.class

Engine.class: Engine.java Hand.java Card.java Pile.java Player.java User.java
	javac Engine.java

test: Engine.class
	java Engine

clean: Engine.class
	rm *.class
	
