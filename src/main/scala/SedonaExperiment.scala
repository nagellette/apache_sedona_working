package org.negengec.sedona_exp

import org.apache.sedona.core.enums.GridType
import org.apache.sedona.core.formatMapper.shapefileParser.ShapefileReader
import org.apache.sedona.core.spatialRDD.SpatialRDD
import org.apache.sedona.sql.utils.{Adapter, SedonaSQLRegistrator}
import org.apache.sedona.viz.core.Serde.SedonaVizKryoRegistrator
import org.apache.sedona.viz.sql.utils.SedonaVizRegistrator
import org.apache.spark.serializer.KryoSerializer
import org.apache.spark.sql.SparkSession
import org.locationtech.jts.geom.Geometry

object SedonaExperiment extends App {

  val sparkSession: SparkSession = SparkSession.builder().config("spark.serializer", classOf[KryoSerializer].getName).
    config("spark.kryo.registrator", classOf[SedonaVizKryoRegistrator].getName).
    master("local[*]").appName("Sedona-Analysis").getOrCreate()

  SedonaSQLRegistrator.registerAll(sparkSession)
  SedonaVizRegistrator.registerAll(sparkSession)

  val resourceFolder = System.getProperty("user.dir")

  val inputRegions = resourceFolder + "/sample_data/qgis_sample_data/shapefiles/regions/"
  val inputAirports = resourceFolder + "/sample_data/qgis_sample_data/shapefiles/airports/"

  var shapeRegions = new SpatialRDD[Geometry]()
  var shapeAirports = new SpatialRDD[Geometry]()

  shapeRegions = ShapefileReader.readToGeometryRDD(sparkSession.sparkContext, inputRegions)
  shapeAirports = ShapefileReader.readToGeometryRDD(sparkSession.sparkContext, inputAirports)

  shapeRegions.analyze()
  shapeAirports.analyze()

  println(shapeAirports.rawSpatialRDD.count())
  println(shapeRegions.rawSpatialRDD.count())

  val useIndex = false
  val considerBoundaryIntersection = false

  shapeRegions.spatialPartitioning(GridType.QUADTREE)
  shapeAirports.spatialPartitioning(shapeRegions.getPartitioner)

  var shapeRegionsDf = Adapter.toDf(shapeRegions,sparkSession)
  var shapeAirportsDf = Adapter.toDf(shapeAirports,sparkSession)

  shapeRegionsDf.show()
  shapeAirportsDf.show()

  shapeRegionsDf.createOrReplaceTempView("regions")
  shapeAirportsDf.createOrReplaceTempView("airports")

  var polygonDf = sparkSession.sql("SELECT regions.NAME_2 as Admin_name, count(airports.geometry) as Airport_count" +
    " FROM regions, airports WHERE ST_Contains(regions.geometry, airports.geometry) group by regions.NAME_2")

  polygonDf.show()

  polygonDf.printSchema()
}
