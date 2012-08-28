import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "imsu"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      "commons-io" % "commons-io" % "1.3.2",
      "com.amazonaws" % "aws-java-sdk" % "1.3.14"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = JAVA).settings(
      // Add your own project settings here      
    )

}
