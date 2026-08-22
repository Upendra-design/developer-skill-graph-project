import { useEffect, useState } from 'react'
import { useParams, Link } from 'react-router-dom'
import { api } from '../api/client.js'
import LoadingState from '../components/LoadingState.jsx'
import ErrorState from '../components/ErrorState.jsx'
import EmptyState from '../components/EmptyState.jsx'
import Tag from '../components/Tag.jsx'

export default function DeveloperDetails() {
  const { id } = useParams()
  const [details, setDetails] = useState(null)
  const [related, setRelated] = useState([])
  const [status, setStatus] = useState('loading')

  const load = () => {
    setStatus('loading')
    Promise.all([api.getDeveloperDetails(id), api.getRelatedDevelopers(id)])
      .then(([detailsRes, relatedRes]) => {
        setDetails(detailsRes.data)
        setRelated(relatedRes.data)
        setStatus('ready')
      })
      .catch((err) => {
        setStatus(err?.response?.status === 404 ? 'not-found' : 'error')
      })
  }

  useEffect(load, [id])

  if (status === 'loading') return <LoadingState label="Loading developer…" />
  if (status === 'not-found') return <EmptyState title="Developer not found" message="This developer may have been removed." />
  if (status === 'error') return <ErrorState message="Could not load this developer from CognoDB." onRetry={load} />

  const { developer, skills, projects, connectedTechnologies, knownTechnologies } = details

  return (
    <div className="page">
      <Link to="/developers" className="back-link">&larr; Back to developers</Link>

      <div className="page-header">
        <h1>{developer.name}</h1>
        <p className="page-subtitle">{developer.email}</p>
      </div>

      <div className="detail-grid">
        <section className="card">
          <h3>Skills</h3>
          {skills.length === 0 ? <p className="muted">No skills recorded.</p> : (
            <div className="tag-list">
              {skills.map((s) => <Tag key={s.id} tone="green">{s.name}</Tag>)}
            </div>
          )}
        </section>

        <section className="card">
          <h3>Known Technologies</h3>
          {knownTechnologies.length === 0 ? <p className="muted">No technologies recorded.</p> : (
            <div className="tag-list">
              {knownTechnologies.map((t) => <Tag key={t.id} tone="blue">{t.name}</Tag>)}
            </div>
          )}
        </section>

        <section className="card">
          <h3>Projects Worked On</h3>
          {projects.length === 0 ? <p className="muted">No projects recorded.</p> : (
            <ul className="simple-list">
              {projects.map((p) => (
                <li key={p.id}>
                  <Link to={`/projects/${p.id}`}>{p.name}</Link>
                  <p className="muted small">{p.description}</p>
                </li>
              ))}
            </ul>
          )}
        </section>

        <section className="card">
          <h3>Connected Technologies <span className="hint">(2-hop traversal)</span></h3>
          <p className="muted small">
            Every technology used by any project this developer has worked on —
            found by walking Developer → WORKED_ON → Project → USES → Technology.
          </p>
          {connectedTechnologies.length === 0 ? <p className="muted">None found.</p> : (
            <div className="tag-list">
              {connectedTechnologies.map((t) => <Tag key={t.id} tone="purple">{t.name}</Tag>)}
            </div>
          )}
        </section>

        <section className="card full-width">
          <h3>Related Developers <span className="hint">(shared project or technology)</span></h3>
          {related.length === 0 ? <p className="muted">No related developers found.</p> : (
            <ul className="simple-list">
              {related.map((r, idx) => (
                <li key={`${r.developerId}-${idx}`}>
                  <Link to={`/developers/${r.developerId}`}>{r.developerName}</Link>
                  <p className="muted small">via {r.sharedTechnology}: {r.viaProject}</p>
                </li>
              ))}
            </ul>
          )}
        </section>
      </div>
    </div>
  )
}
