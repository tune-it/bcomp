ALL=basepc.jar

ALL:	$(ALL)

basepc.jar:
	jar cf basepc.jar `find . -name "*.class"`

install:
	cp style.css index.html basepc.jar /usr/local/www/apache22/data/basepc
