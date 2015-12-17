import dal.{PersonRepository, WithDB}

import org.specs2.mutable._
import org.specs2.specification._

import scala.concurrent.Future
import play.api.inject._
import play.api.Application
import play.api.test._

import play.api.test.Helpers._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import play.api.Play


class PersonRepositoryTestSpec extends PlaySpecification with Inject {
	
	lazy val repo = inject[PersonRepository]
	"A PersonRepository" should {

		"contain the right amount of people." in  {
			println("IN TEST "+ await(repo.list()).length)
			await(repo.create("John Doe",25))
			await(repo.list()).length must beEqualTo(0)
		}
  }
}



import play.api.inject.guice._
import scala.reflect.ClassTag

trait Inject {
  lazy val builder =
    new GuiceApplicationBuilder

  lazy val injector = builder.load(testModule).injector

  lazy val testModule: GuiceableModule =
    new TestModule

  def inject[T : ClassTag]: T =
    injector.instanceOf[T]
   
  def getApp: Application =
  	builder.load(testModule).build
}

import com.google.inject.AbstractModule

class TestModule extends AbstractModule {
  def configure() = {

    bind(classOf[WithDB])
      .to(classOf[WithTestDB])

  }
}

class WithTestDB extends WithDB {
  protected val dbConfig = DatabaseConfigProvider.get[JdbcProfile]("test")(Play.current)
  override def getDBConfig: slick.backend.DatabaseConfig[JdbcProfile] = dbConfig
}
