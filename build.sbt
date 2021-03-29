import sbt.Keys.{libraryDependencies, version}

name := "apache_sedona_working"

version := "0.1"

scalaVersion := "2.12.0"

idePackagePrefix := Some("org.negengec.sedona_exp")

// https://mvnrepository.com/artifact/org.apache.sedona/sedona-core-3.0
libraryDependencies += "org.apache.sedona" %% "sedona-core-3.0" % "1.0.0-incubating"

// https://mvnrepository.com/artifact/org.apache.sedona/sedona-sql-3.0
libraryDependencies += "org.apache.sedona" %% "sedona-sql-3.0" % "1.0.0-incubating"

// https://mvnrepository.com/artifact/org.apache.sedona/sedona-viz-3.0
libraryDependencies += "org.apache.sedona" %% "sedona-viz-3.0" % "1.0.0-incubating"


// https://mvnrepository.com/artifact/org.apache.spark/spark-core
libraryDependencies += "org.apache.spark" %% "spark-core" % "3.0.0"

// https://mvnrepository.com/artifact/org.apache.spark/spark-sql
libraryDependencies += "org.apache.spark" %% "spark-sql" % "3.0.0"

// https://mvnrepository.com/artifact/org.apache.spark/spark-mllib
libraryDependencies += "org.apache.spark" %% "spark-mllib" % "3.0.0" % "provided"

// https://mvnrepository.com/artifact/org.apache.spark/spark-streaming
libraryDependencies += "org.apache.spark" %% "spark-streaming" % "3.0.0" % "provided"

// https://mvnrepository.com/artifact/org.locationtech.jts/jts-core
libraryDependencies += "org.locationtech.jts" % "jts-core" % "1.18.1"  % "compile"

// https://mvnrepository.com/artifact/org.datasyslab/geotools-wrapper
libraryDependencies += "org.datasyslab" % "geotools-wrapper" % "geotools-24.0"  % "compile"

// https://mvnrepository.com/artifact/org.wololo/jts2geojson
libraryDependencies += "org.wololo" % "jts2geojson" % "0.14.3" % "compile"

// https://mvnrepository.com/artifact/org.scala-lang/scala-reflect
libraryDependencies += "org.scala-lang" % "scala-reflect" % "2.12.13" % "compile"


assemblyMergeStrategy in assembly := {
  case PathList("ocrg.apache.sedona", "sedona-core", xs@_*) => MergeStrategy.first
  case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
  case "META-INF/services" => MergeStrategy.last
  case path if path.endsWith(".SF") => MergeStrategy.discard
  case path if path.endsWith(".DSA") => MergeStrategy.discard
  case path if path.endsWith(".RSA") => MergeStrategy.discard
  case _ => MergeStrategy.first
}

resolvers ++= Seq(
  "Open Source Geospatial Foundation Repository" at "https://repo.osgeo.org/repository/release/",
  "Apache Software Foundation Snapshots" at "https://repository.apache.org/content/groups/snapshots",
  "Java.net repository" at "https://download.java.net/maven/2"
)
