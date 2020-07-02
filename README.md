# yq project

![](logo.png)

Yaml files became pretty popular, mainly because they are the main language used for popular tools like Kubernetes or Ansible.

This project is inpired by the [`jq`](https://stedolan.github.io/jq/) tool as a CLI tool for parsing json files.
But the main target of this tool are yaml files.

This tool allows users to apply [`JSONPath`](https://goessner.net/articles/JsonPath/) queries to yaml files. Colaterally this tool allow seamless conversion between `json` and `yaml` files.

## Usage

`yq [-f(ile) <filename>] [-q(uery) <jsonpath>] [-i(nput) <format>] [-o(utput) <format>]`

|               |              |
| :------------ | :----------  |
| f(ile)        | Relative or absolute path for the input file. Currently accepted formats are JSON and YAML. Format is detected by trying to parse de file, not by extension. If this parameter is ommited, standard input is used (press ^D to finish).   |
| q(uery)       | JsonPath query to apply. This can be provided multiple times, so queries are applied to previous query result.  |
| i(nput)       | Expected input format. If present, overrides format autodetection 
| o(utput)      | Format of the output. If ommited, output will retain the same format as the input|

## Formats

Currently supported formats are:

- YAML
- JSON
- TXT
- PROPERTIES

TXT format only for encondes arrays, not hierarchical structures. Each array element is just an string. Array elements are separated by a line break (system dependant).

### Format autodetection:

The algorithm used to determine the format of the input is the following:

1. If the `input` parameter is provided, the contents are tried to be parsed using the specified format. 
2. Else, if the `file` parameter is provided, the extension of the file is used to try to identify the format. If extension matches one of the known standards extensions, the appropriate parser is used.
3. If no `input` nor `file` parameters (or file extension is not recognised), force brute will be used. That is, program will try each parser available. The first successfull parser determines the format of the input.

Note that the third step is unreliable, as many files can be identified by several parsers:
- TXT parser can read ANY file
- PROPERTIES parser can read any YAML file
- YAML parser can read many TXT and PROPERTIES files.

To avoid this situation, it is recommended to provide the `input` parameter to the program.

#### Format extensions

| Format | File extensions |
| :----- | :-------------- |
| JSON   | `.json`         |
| YAML   | `.yml` `.yaml`  |
| PROPERTIES | `.props` `.properties` |
| TXT | `.txt` |


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
--- "file"
```

This is equivalent to redirecting the input:
```
$ yq -q "$.menu.id" < menu.yaml
--- "file"
```

Also equivalent to using pipes:
```
$ cat menu.yaml | yq -q "$.menu.id"
--- "file"
```

Indeed, you can also redirect or pipe the output:
```
$ yq -f menu.yaml -o json > menu.json
````

## Quarkus

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .
