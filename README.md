# A Simple App


lein kibit | lein ancient


lein cljfmt fix


lein do clean, figwheel
http://localhost:3449/example.html

### Production Version

Run "`lein do clean, with-profile prod compile`" to compile an optimised 
version, and then open `resources/public/example.html` in a browser.
