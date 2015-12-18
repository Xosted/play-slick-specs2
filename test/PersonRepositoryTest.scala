import dal.{PersonRepository, WithDB}

import org.specs2.mutable._
import org.specs2.specification._
import play.api.Configuration

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
		  println("IN TEST "+ await(repo.list).length)
//			await(repo.create("John Doe",25))
//			await(repo.list).length must beEqualTo(0)
ok
		}
  }
}



import play.api.inject.guice._
import scala.reflect.ClassTag

trait Inject extends BeforeAfterAll {

  def beforeAll =
    Play.start(application)

  def afterAll =
    application.stop

  lazy val builder =
    (new GuiceApplicationBuilder).
      overrides(bind(classOf[WithDB]).to(classOf[WithTestDB]))

  lazy val injector = builder.injector

  def inject[T : ClassTag]: T =
    injector.instanceOf[T]

  lazy val application: Application =
    builder.build
}

class WithTestDB extends WithDB {
  protected lazy val dbConfig = DatabaseConfigProvider.get[JdbcProfile]("test")(Play.current)
  override def getDBConfig: slick.backend.DatabaseConfig[JdbcProfile] = dbConfig
}
