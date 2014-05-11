/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package objetosNegocio;

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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author IVI
 */
@Entity
@Table(name = "pagos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pago.findAll", query = "SELECT p FROM Pago p"),
    @NamedQuery(name = "Pago.findByIdPagos", query = "SELECT p FROM Pago p WHERE p.idPagos = :idPagos"),
    @NamedQuery(name = "Pago.findByMonto", query = "SELECT p FROM Pago p WHERE p.monto = :monto"),
    @NamedQuery(name = "Pago.findByFecha", query = "SELECT p FROM Pago p WHERE p.fecha = :fecha")})
public class Pago implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idPagos")
    private Integer idPagos;
    @Basic(optional = false)
    @Column(name = "Monto")
    private double monto;
    @Basic(optional = false)
    @Column(name = "Fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @JoinColumn(name = "idAlumno_FK", referencedColumnName = "idAlumnos")
    @ManyToOne(optional = false)
    private Alumno idAlumnoFK;

    public Pago() {
    }

    public Pago(Integer idPagos) {
        this.idPagos = idPagos;
    }

    public Pago(Integer idPagos, double monto, Date fecha) {
        this.idPagos = idPagos;
        this.monto = monto;
        this.fecha = fecha;
    }

    public Integer getIdPagos() {
        return idPagos;
    }

    public void setIdPagos(Integer idPagos) {
        this.idPagos = idPagos;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Alumno getIdAlumnoFK() {
        return idAlumnoFK;
    }

    public void setIdAlumnoFK(Alumno idAlumnoFK) {
        this.idAlumnoFK = idAlumnoFK;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPagos != null ? idPagos.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pago)) {
            return false;
        }
        Pago other = (Pago) object;
        if ((this.idPagos == null && other.idPagos != null) || (this.idPagos != null && !this.idPagos.equals(other.idPagos))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "objetosNegocio.Pago[ idPagos=" + idPagos + " ]";
    }
    
}
