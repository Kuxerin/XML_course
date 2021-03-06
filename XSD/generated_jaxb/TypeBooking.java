//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.11.19 at 08:59:05 AM CET 
//


package fasz;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for Type_Booking complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Type_Booking">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Date_of_booking" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *       &lt;/sequence>
 *       &lt;attribute name="ISBN" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="ReaderID" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Type_Booking", propOrder = {
    "dateOfBooking"
})
public class TypeBooking {

    @XmlElement(name = "Date_of_booking", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dateOfBooking;
    @XmlAttribute(name = "ISBN", required = true)
    protected String isbn;
    @XmlAttribute(name = "ReaderID", required = true)
    protected String readerID;

    /**
     * Gets the value of the dateOfBooking property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateOfBooking() {
        return dateOfBooking;
    }

    /**
     * Sets the value of the dateOfBooking property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateOfBooking(XMLGregorianCalendar value) {
        this.dateOfBooking = value;
    }

    /**
     * Gets the value of the isbn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getISBN() {
        return isbn;
    }

    /**
     * Sets the value of the isbn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setISBN(String value) {
        this.isbn = value;
    }

    /**
     * Gets the value of the readerID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReaderID() {
        return readerID;
    }

    /**
     * Sets the value of the readerID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReaderID(String value) {
        this.readerID = value;
    }

}
