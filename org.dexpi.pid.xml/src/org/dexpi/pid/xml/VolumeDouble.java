//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �nderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2013.10.22 um 01:08:11 PM CEST 
//


package org.dexpi.pid.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse f�r VolumeDouble complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="VolumeDouble">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="Value" type="{http://www.w3.org/2001/XMLSchema}double" />
 *       &lt;attribute name="Units" type="{}VolumeUnitsType" />
 *       &lt;attribute name="ValueURI" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *       &lt;attribute name="UnitsURI" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VolumeDouble")
public class VolumeDouble extends GeneratedClass{

    @XmlAttribute(name = "Value")
    protected java.lang.Double value;
    @XmlAttribute(name = "Units")
    protected VolumeUnitsType units;
    @XmlAttribute(name = "ValueURI")
    @XmlSchemaType(name = "anyURI")
    protected java.lang.String valueURI;
    @XmlAttribute(name = "UnitsURI")
    @XmlSchemaType(name = "anyURI")
    protected java.lang.String unitsURI;

    /**
     * Ruft den Wert der value-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.Double }
     *     
     */
    public java.lang.Double getValue() {
        return this.value;
    }

    /**
     * Legt den Wert der value-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.Double }
     *     
     */
    public void setValue(java.lang.Double value) {
        this.value = value;
    }

    /**
     * Ruft den Wert der units-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link VolumeUnitsType }
     *     
     */
    public VolumeUnitsType getUnits() {
        return this.units;
    }

    /**
     * Legt den Wert der units-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link VolumeUnitsType }
     *     
     */
    public void setUnits(VolumeUnitsType value) {
        this.units = value;
    }

    /**
     * Ruft den Wert der valueURI-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String }
     *     
     */
    public java.lang.String getValueURI() {
        return this.valueURI;
    }

    /**
     * Legt den Wert der valueURI-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String }
     *     
     */
    public void setValueURI(java.lang.String value) {
        this.valueURI = value;
    }

    /**
     * Ruft den Wert der unitsURI-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String }
     *     
     */
    public java.lang.String getUnitsURI() {
        return this.unitsURI;
    }

    /**
     * Legt den Wert der unitsURI-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String }
     *     
     */
    public void setUnitsURI(java.lang.String value) {
        this.unitsURI = value;
    }

}
