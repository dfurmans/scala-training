package info.maalvarez.lesson07

import info.maalvarez.lesson07.Main.Evaluator
import io.circe.Decoder.Result
import io.circe._
import io.circe.generic.semiauto._
import io.circe.parser.decode
import io.circe.syntax._

case class Person(name: String)
case class Greeting(salutation: String, person: Person, exclamationMarks: Int)


//sealed trait ConfirmationTeaserType
//
//object ConfirmationTeaserType {
//  case object Default          extends ConfirmationTeaserType
//  case object DglCard          extends ConfirmationTeaserType
//  case object Newsletter       extends ConfirmationTeaserType
//  case object AffiliateVoucher extends ConfirmationTeaserType
//
//  implicit val decode: Decoder[ConfirmationTeaserType] = new Decoder[ConfirmationTeaserType] {
//    override def apply(c: HCursor): Result[ConfirmationTeaserType] =
//      for {
//        confirmationTeaserType <- c.downField("value").as[String].right
//      } yield confirmationTeaserType match {
//        case "Default"          => Default
//        case "DglCard"          => DglCard
//        case "Newsletter"       => Newsletter
//        case "AffiliateVoucher" => AffiliateVoucher
//      }
//  }
//
//  implicit val encode: Encoder[ConfirmationTeaserType] = new Encoder[ConfirmationTeaserType] {
//    override def apply(confirmationTeaserType: ConfirmationTeaserType): Json = confirmationTeaserType.toString.asJson
//  }
//}


// ADT for the Delivery
sealed trait DeliveryStatus
object DeliveryStatus{
  case object WAITING_IN_HUB extends DeliveryStatus
  case object DELIVERED extends DeliveryStatus

  def apply(deliveryTypeAsString: String): DeliveryStatus = deliveryTypeAsString match {
    case "WAITING_IN_HUB" => WAITING_IN_HUB
    case "DELIVERED"      => DELIVERED
  }

  implicit val decode: Decoder[DeliveryStatus] = new Decoder[DeliveryStatus] {
    final def apply(c: HCursor): Result[DeliveryStatus] =
      for {
        aDeliveryStatusType <- c.downField("status").as[String].right
      } yield DeliveryStatus(aDeliveryStatusType)
  }

  implicit val encode: Encoder[DeliveryStatus] = new Encoder[DeliveryStatus] {
    override def apply(a: DeliveryStatus): Json = a match {
      case WAITING_IN_HUB => "WAITING_IN_HUB".asJson
      case DELIVERED      => "DELIVERED".asJson
    }
  }

}

sealed trait TrackIt
case class Tracking(status: DeliveryStatus,
                    parcels: Int,
                    weight: Option[Int],
                    reference: String) extends TrackIt

object Tracking{
  implicit val decode: Decoder[Tracking] = deriveDecoder[Tracking]
  implicit val encode: Encoder[Tracking] = deriveEncoder[Tracking]
}

case class Foo(a: Int, b: String, c: Boolean)

object Main {
  implicit val fooDecoder: Decoder[Foo] = deriveDecoder
  implicit val fooEncoder: Encoder[Foo] = deriveEncoder

  val t1 = Tracking(status = DeliveryStatus.DELIVERED,
    parcels =12,
    weight = Some(12),
    reference =  "ASD123"
  )

  val t2 = Tracking(status = DeliveryStatus.DELIVERED,
    parcels =12,
    weight = Some(12),
    reference =  "ASB123"
  )

  case class Parcel(weight: Int, width: Int, height: Int, length: Int)
  case class Shipment(reference : String, parcels: List[Parcel])

  println(Shipment.getClass.getSimpleName)
  val s1 = Shipment("ASD123", parcels = List.empty[Parcel])
  val sds : Boolean = 0 < 0
  println("asdadasdsd" + sds )

  sealed trait TrackingCase
  object TrackingCase{
    case object CONCILLIATION_REQUEST extends TrackingCase
    case object NOT_NEEDED extends TrackingCase
    case object INCOMPLETE extends TrackingCase
    case object NOT_FOUND extends TrackingCase
  }
  sealed trait EventIssue
  case object EmptyEvent extends EventIssue
  case class TrackingEvent(reference: String, status: TrackingCase) extends EventIssue

  trait EventIssueDispatchCriterion{
    type CriteriaType = (Shipment, Tracking) => List[Boolean]
    val criterions : CriteriaType =
      (aShipment, aTrackingBusiness) => {
        val criterion1  = {
          aShipment.reference == aTrackingBusiness.reference &&
          aShipment.parcels.length == aTrackingBusiness.parcels &&
          aShipment.parcels.foldRight(0)((x, y) => x.weight + y) >= aTrackingBusiness.weight.getOrElse(0)
          }
        val criterion2 = {
          {
            aShipment.reference == aTrackingBusiness.reference &&
            aShipment.parcels.length == aTrackingBusiness.parcels &&
            aShipment.parcels.foldRight(0)((x, y) => x.weight + y) <= aTrackingBusiness.weight.getOrElse(0)
          }
        }
        val criterion3 = {
          {
            aShipment.reference == aTrackingBusiness.reference
          }
        }
        List(criterion1, criterion2, criterion3)
      }

    private def evaluateCriterionDefinition(a: Shipment, t: Tracking): CriteriaType => List[Boolean] = listOfCriterions => {
       listOfCriterions.apply(a,t)
    }
    def performCriterionEvaluation(a: Shipment, t: Tracking): List[Boolean] = {
      this.evaluateCriterionDefinition(a,t)(this.criterions)
    }
  }
  object Evaluator extends EventIssueDispatchCriterion{
    def doIT = performCriterionEvaluation(s1,t1)
  }

  println("Ewolucja " + Evaluator.doIT)

  val s = Shipment("", parcels = List(
    Parcel(10,2,3,4),
    Parcel(1,2,3,4),
    Parcel(1,2,3,4),
    Parcel(1,2,3,4)
    )
  )
  println {
    println("Suma to :: ")
    s.parcels.foldRight(0)((x, y) => x.weight + y)
  }

  println("Sprawdzam: EQUALS" + t1.equals(t2))
  println("Sprawdzam:" + t1 == t2)

  def main(args: Array[String]): Unit = {

    val aTracking =
      """
        {
          "status":"DELIVERED",
          "parcels":2,
          "weight":30,
          "reference":"ABCD123456"
        }
        """
    val trackingDecode = decode[Tracking](aTracking)
    println(trackingDecode)




    //
//    val rawJson: String = """{
//        "foo": "bar",
//        "baz": 123,
//        "list of stuff": [ 4, 5, 6 ]
//      }"""
//
//    val parseResult = parse(rawJson)
//
//    parseResult match {
//      case Left(_) => println("Invalid JSON :(")
//      case Right(_) => println("Yay, got some JSON!")
//    }
//
//
//    //////////////////////////////////////////////////
//
//    val intsJson = List(1, 2, 3).asJson
//
//    println(intsJson)
//
//    intsJson.as[List[Int]] match {
//      case Right(value) => println(value)
//    }
//
//    //////////////////////////////////////////////////
//
//    Greeting("Hey", Person("Chris"), 3).asJson.as[Greeting] match {
//      case Right(value) => println(s"HOLAAAAA: $value")
//    }
//
//    //////////////////////////////////////////////////
//
//    println(Foo(1, "a", true).asJson.as[Foo])
  }
}