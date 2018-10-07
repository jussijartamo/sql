# SQL Editor

Setup test environment and open page [http://localhost:4450/sql.html](http://localhost:4450/sql.html)

```
lein less auto
lein frontend
lein example-datasource
lein server
```

Check versions
```
lein ancient
```

Display dependencies
```
lein deps :tree
```

Check style
```
lein kibit
```

Format code
```
lein cljfmt fix
```

## TODO

NEXT
- [ ] Auto memoize top 5 querys. UI element to release result from memory. Checkbox to permanently keep.

UI
- [ ] Use space efficiently. At least table should use whole window.
- [ ] DB selector. Drag'n'drop JDBC URLs.
- [ ] Execute query button
- [ ] Execute update button
- [ ] Use accordions to hide/display less meaningful data. Such as schema columns.
- [ ] Switch between SQL and scripting modes
- [ ] Display query status
- [ ] Display stack traces
- [ ] Display ajax spinner
- [ ] Animations
- [ ] Cleanup LESS
- [ ] Display result count
- [ ] Lazy load result into UI

APP
- [ ] Feature to store multiple query results
- [ ] Fix disconnect between last query and query result on UI.
- [ ] Support code evaluation
- [ ] Cleanup core.clj. Remove handlers from core.
- [ ] End to end tests
- [ ] Better testing tools
- [ ] DB pool
- [ ] Lazy result loading



