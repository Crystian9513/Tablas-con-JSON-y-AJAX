/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Peralta
 */
@Entity
@Table(name = "aprendiz")
@NamedQueries({
    @NamedQuery(name = "Aprendiz.findAll", query = "SELECT a FROM Aprendiz a"),
    @NamedQuery(name = "Aprendiz.findByCedula", query = "SELECT a FROM Aprendiz a WHERE a.cedula = :cedula"),
    @NamedQuery(name = "Aprendiz.findByNombres", query = "SELECT a FROM Aprendiz a WHERE a.nombres = :nombres"),
    @NamedQuery(name = "Aprendiz.findByFecha", query = "SELECT a FROM Aprendiz a WHERE a.fecha = :fecha")})
public class Aprendiz implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "CEDULA")
    private Integer cedula;
    @Basic(optional = false)
    @Column(name = "NOMBRES")
    private String nombres;
    @Column(name = "FECHA")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @JoinColumn(name = "FORMACION_CODIGO", referencedColumnName = "CODIGO")
    @ManyToOne(optional = false)
    private Formacion formacionCodigo;
    @JoinColumn(name = "Sede_CODIGO", referencedColumnName = "CODIGO")
    @ManyToOne(optional = false)
    private Sede sedeCODIGO;

    public Aprendiz() {
    }

    public Aprendiz(Integer cedula) {
        this.cedula = cedula;
    }

    public Aprendiz(Integer cedula, String nombres) {
        this.cedula = cedula;
        this.nombres = nombres;
    }

    public Integer getCedula() {
        return cedula;
    }

    public void setCedula(Integer cedula) {
        this.cedula = cedula;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Formacion getFormacionCodigo() {
        return formacionCodigo;
    }

    public void setFormacionCodigo(Formacion formacionCodigo) {
        this.formacionCodigo = formacionCodigo;
    }

    public Sede getSedeCODIGO() {
        return sedeCODIGO;
    }

    public void setSedeCODIGO(Sede sedeCODIGO) {
        this.sedeCODIGO = sedeCODIGO;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cedula != null ? cedula.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Aprendiz)) {
            return false;
        }
        Aprendiz other = (Aprendiz) object;
        if ((this.cedula == null && other.cedula != null) || (this.cedula != null && !this.cedula.equals(other.cedula))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Aprendiz[ cedula=" + cedula + " ]";
    }
    
}
