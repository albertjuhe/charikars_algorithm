<?xml version="1.0"?>

<!--
	Aquest xsl s'encarrega de extreure tots els blocs de texte de cada document.

-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:xalan="org.apache.xalan.xslt.extensions.Redirect"
	extension-element-prefixes="xalan">
	
	<xsl:output method="text"/>
	
	<!--
		Template principal main()
	-->
	<xsl:template match="/">
		<xsl:apply-templates select="assignatura"/>
	</xsl:template>

	<xsl:template match="assignatura">
		<xsl:apply-templates select="contingut | introduccio | objectius"/>
	</xsl:template>

	<xsl:template match="contingut">
		<xsl:apply-templates/>
	</xsl:template>	

	<xsl:template match="modul">
		<xsl:apply-templates/>
	</xsl:template>
	
	<xsl:template match="introduccio">
		<xsl:apply-templates/>
	</xsl:template>
	
	<xsl:template match="objectius">
		<xsl:apply-templates/>
	</xsl:template>

	<xsl:template match="resum">
		<xsl:apply-templates/>
	</xsl:template>

	<xsl:template match="unit | subunit | sub-subunit | sub-sub-subunit | sub-sub-sub-subunit">
		<xsl:apply-templates/>
	</xsl:template>

	<xsl:template match="recurs | text">
		<xsl:apply-templates/>	
	</xsl:template>

	<xsl:template match="sub-recurs">
		<xsl:apply-templates/>
	</xsl:template>


	<xsl:template match="p | ol | ul | li">
		<xsl:apply-templates/>
	</xsl:template>

	<xsl:template match="title">
		<xsl:apply-templates/>
	</xsl:template>


	<xsl:template match="b | u | i">
		<xsl:apply-templates/>
	</xsl:template>

	<xsl:template match="metadades | activitats | autoevaluacio | solucionari"/>
   
</xsl:stylesheet>
