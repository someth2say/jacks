package org.someth2say;

enum Format {
    JSON(new Json()), YAML(new Yaml()),  PLAIN(new Plain());

    public FormatMapper mapper;

	Format(FormatMapper mapper){
		this.mapper = mapper;
    }

}