import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder
import scala.concurrent.duration._

class GithubGatewaySimulator extends Simulation {
   private val baseUrl = "http://54.214.149.134:8080"
   private val contentType = "application/json"
   private val endpoint = "/repositories/protocolbuffers/protobuf"
   private val index = "/"
   private val requestCount = 30 


 val httpProtocol = http
   .baseUrl(baseUrl)
   .inferHtmlResources()
   .acceptHeader("*/*")
   .contentTypeHeader(contentType)
   .userAgentHeader("curl/7.54.0")

 val scn: ScenarioBuilder = scenario("RecordedSimulation")
     .exec(http("get_repository_200_or403")
     .get(endpoint)
     .check(status.in(200, 403)))

 setUp(scn.inject(constantUsersPerSec(requestCount) during (1 minutes) randomized)).protocols(httpProtocol)
}
