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
@Table(name = "catalogos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Catalogo.findAll", query = "SELECT c FROM Catalogo c"),
    @NamedQuery(name = "Catalogo.findByIdCatalogo", query = "SELECT c FROM Catalogo c WHERE c.idCatalogo = :idCatalogo"),
    @NamedQuery(name = "Catalogo.findByHora", query = "SELECT c FROM Catalogo c WHERE c.hora = :hora"),
    @NamedQuery(name = "Catalogo.findByAula", query = "SELECT c FROM Catalogo c WHERE c.aula = :aula"),
    @NamedQuery(name = "Catalogo.findByPerior", query = "SELECT c FROM Catalogo c WHERE c.perior = :perior")})
public class Catalogo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idCatalogo")
    private Integer idCatalogo;
    @Basic(optional = false)
    @Column(name = "Hora")
    private String hora;
    @Basic(optional = false)
    @Column(name = "Aula")
    private String aula;
    @Basic(optional = false)
    @Column(name = "Perior")
    private String perior;
    @JoinColumn(name = "idMateria_FK", referencedColumnName = "idMateria")
    @ManyToOne
    private Materia idMateriaFK;
    @JoinColumn(name = "idMaestro_FK", referencedColumnName = "idMaestros")
    @ManyToOne
    private Maestro idMaestroFK;
    @OneToMany(mappedBy = "idCatalogoFK")
    private Collection<Grupo> grupoCollection;

    public Catalogo() {
    }

    public Catalogo(Integer idCatalogo) {
        this.idCatalogo = idCatalogo;
    }

    public Catalogo(Integer idCatalogo, String hora, String aula, String perior) {
        this.idCatalogo = idCatalogo;
        this.hora = hora;
        this.aula = aula;
        this.perior = perior;
    }

    public Integer getIdCatalogo() {
        return idCatalogo;
    }

    public void setIdCatalogo(Integer idCatalogo) {
        this.idCatalogo = idCatalogo;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getAula() {
        return aula;
    }

    public void setAula(String aula) {
        this.aula = aula;
    }

    public String getPerior() {
        return perior;
    }

    public void setPerior(String perior) {
        this.perior = perior;
    }

    public Materia getIdMateriaFK() {
        return idMateriaFK;
    }

    public void setIdMateriaFK(Materia idMateriaFK) {
        this.idMateriaFK = idMateriaFK;
    }

    public Maestro getIdMaestroFK() {
        return idMaestroFK;
    }

    public void setIdMaestroFK(Maestro idMaestroFK) {
        this.idMaestroFK = idMaestroFK;
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
        hash += (idCatalogo != null ? idCatalogo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Catalogo)) {
            return false;
        }
        Catalogo other = (Catalogo) object;
        if ((this.idCatalogo == null && other.idCatalogo != null) || (this.idCatalogo != null && !this.idCatalogo.equals(other.idCatalogo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "objetosNegocio.Catalogo[ idCatalogo=" + idCatalogo + " ]";
    }
    
}
