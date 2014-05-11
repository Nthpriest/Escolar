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
@Table(name = "adeudos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Adeudo.findAll", query = "SELECT a FROM Adeudo a"),
    @NamedQuery(name = "Adeudo.findByIdAdeudos", query = "SELECT a FROM Adeudo a WHERE a.idAdeudos = :idAdeudos"),
    @NamedQuery(name = "Adeudo.findByMonto", query = "SELECT a FROM Adeudo a WHERE a.monto = :monto"),
    @NamedQuery(name = "Adeudo.findByFecha", query = "SELECT a FROM Adeudo a WHERE a.fecha = :fecha"),
    @NamedQuery(name = "Adeudo.findByMotivo", query = "SELECT a FROM Adeudo a WHERE a.motivo = :motivo"),
    @NamedQuery(name = "Adeudo.findByFechaPago", query = "SELECT a FROM Adeudo a WHERE a.fechaPago = :fechaPago")})
public class Adeudo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idAdeudos")
    private Integer idAdeudos;
    @Basic(optional = false)
    @Column(name = "Monto")
    private double monto;
    @Basic(optional = false)
    @Column(name = "Fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Basic(optional = false)
    @Column(name = "Motivo")
    private String motivo;
    @Basic(optional = false)
    @Column(name = "FechaPago")
    private String fechaPago;
    @JoinColumn(name = "idAlumno_FK", referencedColumnName = "idAlumnos")
    @ManyToOne(optional = false)
    private Alumno idAlumnoFK;

    public Adeudo() {
    }

    public Adeudo(Integer idAdeudos) {
        this.idAdeudos = idAdeudos;
    }

    public Adeudo(Integer idAdeudos, double monto, Date fecha, String motivo, String fechaPago) {
        this.idAdeudos = idAdeudos;
        this.monto = monto;
        this.fecha = fecha;
        this.motivo = motivo;
        this.fechaPago = fechaPago;
    }

    public Integer getIdAdeudos() {
        return idAdeudos;
    }

    public void setIdAdeudos(Integer idAdeudos) {
        this.idAdeudos = idAdeudos;
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

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(String fechaPago) {
        this.fechaPago = fechaPago;
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
        hash += (idAdeudos != null ? idAdeudos.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Adeudo)) {
            return false;
        }
        Adeudo other = (Adeudo) object;
        if ((this.idAdeudos == null && other.idAdeudos != null) || (this.idAdeudos != null && !this.idAdeudos.equals(other.idAdeudos))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "objetosNegocio.Adeudo[ idAdeudos=" + idAdeudos + " ]";
    }
    
}
