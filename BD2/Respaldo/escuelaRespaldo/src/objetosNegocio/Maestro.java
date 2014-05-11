/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package objetosNegocio;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author IVI
 */
@Entity
@Table(name = "maestros")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Maestro.findAll", query = "SELECT m FROM Maestro m"),
    @NamedQuery(name = "Maestro.findByIdMaestros", query = "SELECT m FROM Maestro m WHERE m.idMaestros = :idMaestros"),
    @NamedQuery(name = "Maestro.findByNombre", query = "SELECT m FROM Maestro m WHERE m.nombre = :nombre"),
    @NamedQuery(name = "Maestro.findByDepartamento", query = "SELECT m FROM Maestro m WHERE m.departamento = :departamento")})
public class Maestro implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idMaestros")
    private Integer idMaestros;
    @Basic(optional = false)
    @Column(name = "Nombre")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "Departamento")
    private String departamento;
    @OneToMany(mappedBy = "idMaestroFK")
    private Collection<Catalogo> catalogoCollection;

    public Maestro() {
    }

    public Maestro(Integer idMaestros) {
        this.idMaestros = idMaestros;
    }

    public Maestro(Integer idMaestros, String nombre, String departamento) {
        this.idMaestros = idMaestros;
        this.nombre = nombre;
        this.departamento = departamento;
    }

    public Integer getIdMaestros() {
        return idMaestros;
    }

    public void setIdMaestros(Integer idMaestros) {
        this.idMaestros = idMaestros;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    @XmlTransient
    public Collection<Catalogo> getCatalogoCollection() {
        return catalogoCollection;
    }

    public void setCatalogoCollection(Collection<Catalogo> catalogoCollection) {
        this.catalogoCollection = catalogoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMaestros != null ? idMaestros.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Maestro)) {
            return false;
        }
        Maestro other = (Maestro) object;
        if ((this.idMaestros == null && other.idMaestros != null) || (this.idMaestros != null && !this.idMaestros.equals(other.idMaestros))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "objetosNegocio.Maestro[ idMaestros=" + idMaestros + " ]";
    }
    
}
