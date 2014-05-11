/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package objetosNegocio;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Adrian
 */
@Entity
@Table(name = "alumnos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Alumno.findAll", query = "SELECT a FROM Alumno a"),
    @NamedQuery(name = "Alumno.findByIdAlumnos", query = "SELECT a FROM Alumno a WHERE a.idAlumnos = :idAlumnos"),
    @NamedQuery(name = "Alumno.findByNombre", query = "SELECT a FROM Alumno a WHERE a.nombre = :nombre")})
public class Alumno implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idAlumnos")
    private Integer idAlumnos;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @JoinColumn(name = "idCarrera_FK", referencedColumnName = "idCarrera")
    @ManyToOne
    private Carrera idCarreraFK;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idAlumnoFK")
    private Collection<Pago> pagoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idAlumnoFK")
    private Collection<Adeudo> adeudoCollection;
    @OneToMany(mappedBy = "idAlumnoFK")
    private Collection<Grupo> grupoCollection;

    public Alumno() {
    }

    public Alumno(Integer idAlumnos) {
        this.idAlumnos = idAlumnos;
    }

    public Alumno(Integer idAlumnos, String nombre) {
        this.idAlumnos = idAlumnos;
        this.nombre = nombre;
    }

    public Integer getIdAlumnos() {
        return idAlumnos;
    }

    public void setIdAlumnos(Integer idAlumnos) {
        this.idAlumnos = idAlumnos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Carrera getIdCarreraFK() {
        return idCarreraFK;
    }

    public void setIdCarreraFK(Carrera idCarreraFK) {
        this.idCarreraFK = idCarreraFK;
    }

    @XmlTransient
    public Collection<Pago> getPagoCollection() {
        return pagoCollection;
    }

    public void setPagoCollection(Collection<Pago> pagoCollection) {
        this.pagoCollection = pagoCollection;
    }

    @XmlTransient
    public Collection<Adeudo> getAdeudoCollection() {
        return adeudoCollection;
    }

    public void setAdeudoCollection(Collection<Adeudo> adeudoCollection) {
        this.adeudoCollection = adeudoCollection;
    }

    @XmlTransient
    public Collection<Grupo> getGrupoCollection() {
        return grupoCollection;
    }

    public void setGrupoCollection(Collection<Grupo> grupoCollection) {
        this.grupoCollection = grupoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAlumnos != null ? idAlumnos.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Alumno)) {
            return false;
        }
        Alumno other = (Alumno) object;
        if ((this.idAlumnos == null && other.idAlumnos != null) || (this.idAlumnos != null && !this.idAlumnos.equals(other.idAlumnos))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "objetosNegocio.Alumno[ idAlumnos=" + idAlumnos + " ]";
    }
    
}
