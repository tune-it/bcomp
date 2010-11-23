ALL=classes basepc.zip

ALL:	$(ALL)

classes:
	cd bin && make

basepc.zip:	bin/EUI.class bin/style.css Makefile
	cp bin/style.css basepc
	cd bin && jar cvf ../basepc/basepc.jar `find . -name "*.class"`
	rm -f basepc.zip
	zip -9 basepc.zip basepc/*

clean:
	find bin -name "*.class" -delete
	rm -rf basepc.zip basepc/basepc.jar basepc/style.css bin/Machine
