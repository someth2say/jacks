# Jacks

Yaml files became pretty popular, mainly because they are the main language used for popular tools like Kubernetes or Ansible. But many other hierarchical formats are still much used in the wild: JSON, XML, Properties, TOML...

This project is inspired by the [`jq`](https://stedolan.github.io/jq/) tool as a CLI tool for parsing json files.
But the main difference is that is not target to just JSON files, but any kind of hierarchical configuration file.

This tool allows users to apply [`JSONPath`](https://goessner.net/articles/JsonPath/) queries to yaml files. Collaterally this tool allows seamless conversion between `json`,`yaml` or other hierarchical formats.

## Usage

`jacks [-f(ile) <filename>] [-q(uery) <jsonpath>] [-i(nput) <format>] [-o(utput) <format>]`

|          |                                                                                                                                                                                           |
|:---------|:------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| f(ile)   | Relative or absolute path for the input file. Format is detected by trying to parse de file, not by extension. If this parameter is omitted, standard input is used (press ^D to finish). |
| q(uery)  | JsonPath query to apply. This can be provided multiple times, so queries are applied to previous query result.                                                                            |
| i(nput)  | Expected input format. If present, overrides format autodetect                                                                                                                            |
| o(utput) | Format of the output. If omitted, output will retain the same format as the input                                                                                                         |

## Formats

Currently, supported formats are:

- YAML
- JSON
- XML
- PROPERTIES
- TXT*

TXT format only encodes array objects, not hierarchical structures.
Each element in the array is just a string. Array elements are separated by a line break (system dependant).

Future version of this tool will include more formats, such as [TOML](https://toml.io/).

### Format autodetect:

The algorithm used to determine the format of the input is the following:

1. If the `input` parameter is provided, then it tries to parse the content using the specified format. 
2. Else, if the `file` parameter is provided, the extension of the file is used to try to identify the format. If extension matches one of the known standards extensions, the appropriate parser is used.
3. If no `input` nor `file` parameters (or file extension is not recognised), force brute will be used. That is, program will try each parser available. The first successfully parser determines the format of the input.

Note that the third step is unreliable, as many files can be identified by several parsers:
- TXT parser can read ANY file
- PROPERTIES parser can read any YAML file
- YAML parser can read many TXT and PROPERTIES files.

To avoid this situation, it is recommended to provide the `input` parameter to the program.

#### Format extensions and priorities

The following table show the order of formats when trying brute force format detection, as well as the filename extensions associated with each format.

| Format     | File extensions        |
|:-----------|:-----------------------|
| JSON       | `.json`                |
| YAML       | `.yml` `.yaml`         |
| XML        | `.xml`                 |
| PROPERTIES | `.props` `.properties` |
| TXT        | `.txt`                 |


## Examples
* Tranforming yaml to json
```
$ jacks -o json
format: yaml
^D
{
  "format" : "yaml"
}                   
```

* Simple jsonquery
```
$ jacks -q "$.name"
name: $ jacks 
version: 1.0
^D
"jacks"
```

* Reading from files
```
$ jacks -f menu.yaml -q "$.menu.id"
"file"
```

This is equivalent to redirecting the input:
```
$ jacks -q "$.menu.id" < menu.yaml
"file"
```

Also, equivalent to using pipes:
```
$ cat menu.yaml | jacks-q "$.menu.id"
"file"
```

Indeed, you can also redirect or pipe the output:
```
$ jacks -f menu.yaml -o json > menu.json
````

## Quarkus

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .
