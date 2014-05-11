/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package objetosNegocio;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Adrian
 */
@Entity
@Table(name = "grupos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Grupo.findAll", query = "SELECT g FROM Grupo g"),
    @NamedQuery(name = "Grupo.findByIdGrupos", query = "SELECT g FROM Grupo g WHERE g.idGrupos = :idGrupos")})
public class Grupo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idGrupos")
    private Integer idGrupos;
    @JoinColumn(name = "idCatalogo_FK", referencedColumnName = "idCatalogo")
    @ManyToOne
    private Catalogo idCatalogoFK;
    @JoinColumn(name = "idAlumno_FK", referencedColumnName = "idAlumnos")
    @ManyToOne
    private Alumno idAlumnoFK;

    public Grupo() {
    }

    public Grupo(Integer idGrupos) {
        this.idGrupos = idGrupos;
    }

    public Integer getIdGrupos() {
        return idGrupos;
    }

    public void setIdGrupos(Integer idGrupos) {
        this.idGrupos = idGrupos;
    }

    public Catalogo getIdCatalogoFK() {
        return idCatalogoFK;
    }

    public void setIdCatalogoFK(Catalogo idCatalogoFK) {
        this.idCatalogoFK = idCatalogoFK;
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
        hash += (idGrupos != null ? idGrupos.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Grupo)) {
            return false;
        }
        Grupo other = (Grupo) object;
        if ((this.idGrupos == null && other.idGrupos != null) || (this.idGrupos != null && !this.idGrupos.equals(other.idGrupos))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "objetosNegocio.Grupo[ idGrupos=" + idGrupos + " ]";
    }
    
}
