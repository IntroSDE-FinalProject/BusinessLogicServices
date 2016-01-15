//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.01.15 at 12:19:26 PM CET 
//


package introsde.finalproject.rest.generated;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the introsde.finalproject.rest.generated package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _People_QNAME = new QName("", "people");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: introsde.finalproject.rest.generated
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ListPersonType }
     * 
     */
    public ListPersonType createListPersonType() {
        return new ListPersonType();
    }

    /**
     * Create an instance of {@link FamilyType }
     * 
     */
    public FamilyType createFamilyType() {
        return new FamilyType();
    }

    /**
     * Create an instance of {@link DoctorType }
     * 
     */
    public DoctorType createDoctorType() {
        return new DoctorType();
    }

    /**
     * Create an instance of {@link ReminderType }
     * 
     */
    public ReminderType createReminderType() {
        return new ReminderType();
    }

    /**
     * Create an instance of {@link TargetType }
     * 
     */
    public TargetType createTargetType() {
        return new TargetType();
    }

    /**
     * Create an instance of {@link MeasureDefinitionType }
     * 
     */
    public MeasureDefinitionType createMeasureDefinitionType() {
        return new MeasureDefinitionType();
    }

    /**
     * Create an instance of {@link ListMeasureDefinitionType }
     * 
     */
    public ListMeasureDefinitionType createListMeasureDefinitionType() {
        return new ListMeasureDefinitionType();
    }

    /**
     * Create an instance of {@link ListMeasureType }
     * 
     */
    public ListMeasureType createListMeasureType() {
        return new ListMeasureType();
    }

    /**
     * Create an instance of {@link ListReminderType }
     * 
     */
    public ListReminderType createListReminderType() {
        return new ListReminderType();
    }

    /**
     * Create an instance of {@link ListTargetType }
     * 
     */
    public ListTargetType createListTargetType() {
        return new ListTargetType();
    }

    /**
     * Create an instance of {@link PersonType }
     * 
     */
    public PersonType createPersonType() {
        return new PersonType();
    }

    /**
     * Create an instance of {@link MeasureType }
     * 
     */
    public MeasureType createMeasureType() {
        return new MeasureType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListPersonType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "people")
    public JAXBElement<ListPersonType> createPeople(ListPersonType value) {
        return new JAXBElement<ListPersonType>(_People_QNAME, ListPersonType.class, null, value);
    }

}