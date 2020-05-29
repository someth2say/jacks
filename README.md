# yq project

![](logo.png)

Yaml files became pretty popular, mainly because they are the main language used for popular tools like Kubernetes or Ansible.

This project is inpired by the [`jq`](https://stedolan.github.io/jq/) tool as a CLI tool for parsing json files.
But the main target of this tool are yaml files.

This tool allows users to apply [`JSONPath`](https://goessner.net/articles/JsonPath/) queries to yaml files. Colaterally this tool allow seamless conversion between `json` and `yaml` files.

## Usage

`yq [-f(ile) <filename>] [-q(uery) <jsonpath>] [-o(utput) <json|yaml>]`

|               |              |
| :------------ | :----------  |
| f(ile)        | Relative or absolute path for the input file. Currently accepted formats are JSON and YAML. Format is detected by trying to parse de file, not by extension. If this parameter is ommited, standard input is used (press ^D to finish).   |
| q(uery)       | JsonPath query to apply. This can be provided multiple times, so queries are applied to previous query result.  |
| o(utput)      | Format of the output. Currently accepted formats are JSON and YAML |

## Examples
* Tranforming yaml to json
```
$ yq -o json
format: yaml
^D
{
  "format" : "yaml"
}                   
```

* Simple query
```
$ yq -q "$.name"
name: yq
version: 1.0
^D
--- "yq"
```

* Reading from files
```
$ yq -f menu.yaml -q "$.menu.id"
---
menuitem:
- value: "New"
  onclick: "CreateNewDoc()"
- value: "Open"
  onclick: "OpenDoc()"
- value: "Close"
  onclick: "CloseDoc()"
  ```

This is equivalent to redirecting the input:
```
$ yq -q "$.menu.id" < menu.yaml
---
menuitem:
- value: "New"
...
```

Also equivalent to using pipes:
```
$ cat menu.yaml | yq -q "$.menu.id"
---
menuitem:
- value: "New"
...
```

Indeed, you can also pipe the output:
```
$ yq -f menu.yaml -o json > menu.json
````


## Quarkus

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .
