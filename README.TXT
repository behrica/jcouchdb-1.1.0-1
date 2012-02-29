this is the jcouchdb source code. it can be build with ant.

you can import this as project into Eclipse.

contents:
---------

LICENSE.TXT             copy of the BSD license
build.properties        settings for the build process
build.xml               ant build file
ivy.xml			ivy dependency definition
lib                     contains the dependencies
src                     contains the source code
test                    contains unit tests, 1 integration test that will 
                        only pass if run against a real couchdb 
                        (default it runs against localhost)

