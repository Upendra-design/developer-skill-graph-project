import { useEffect, useState } from 'react'
import { useParams, Link } from 'react-router-dom'
import { api } from '../api/client.js'
import LoadingState from '../components/LoadingState.jsx'
import ErrorState from '../components/ErrorState.jsx'
import EmptyState from '../components/EmptyState.jsx'
import Tag from '../components/Tag.jsx'

export default function ProjectDetails() {
  const { id } = useParams()
  const [details, setDetails] = useState(null)
  const [candidates, setCandidates] = useState([])
  const [status, setStatus] = useState('loading')

  const load = () => {
    setStatus('loading')
    Promise.all([api.getProjectDetails(id), api.getCandidateDevelopers(id)])
      .then(([detailsRes, candidatesRes]) => {
        setDetails(detailsRes.data)
        setCandidates(candidatesRes.data)
        setStatus('ready')
      })
      .catch((err) => {
        setStatus(err?.response?.status === 404 ? 'not-found' : 'error')
      })
  }

  useEffect(load, [id])

  if (status === 'loading') return <LoadingState label="Loading project…" />
  if (status === 'not-found') return <EmptyState title="Project not found" message="This project may have been removed." />
  if (status === 'error') return <ErrorState message="Could not load this project from CognoDB." onRetry={load} />

  const { project, technologies, developers } = details

  return (
    <div className="page">
      <Link to="/projects" className="back-link">&larr; Back to projects</Link>

      <div className="page-header">
        <h1>{project.name}</h1>
        <p className="page-subtitle">{project.description}</p>
      </div>

      <div className="detail-grid">
        <section className="card">
          <h3>Technologies Used</h3>
          {technologies.length === 0 ? <p className="muted">None recorded.</p> : (
            <div className="tag-list">
              {technologies.map((t) => <Tag key={t.id} tone="blue">{t.name}</Tag>)}
            </div>
          )}
        </section>

        <section className="card">
          <h3>Developers Who Worked On This</h3>
          {developers.length === 0 ? <p className="muted">None recorded.</p> : (
            <ul className="simple-list">
              {developers.map((d) => (
                <li key={d.id}><Link to={`/developers/${d.id}`}>{d.name}</Link></li>
              ))}
            </ul>
          )}
        </section>

        <section className="card full-width">
          <h3>Candidate Developers <span className="hint">(3-hop, anti-join traversal)</span></h3>
          <p className="muted small">
            Developers who already know a technology this project uses, but haven't
            worked on it yet — found via Project → USES → Technology ← KNOWS ← Developer,
            excluding anyone already connected by WORKED_ON. This kind of "connected but
            not yet linked" query needs a chain of joins plus a NOT EXISTS anti-join in
            SQL; here it's one pattern and one WHERE NOT clause.
          </p>
          {candidates.length === 0 ? <p className="muted">No candidates found.</p> : (
            <div className="tag-list">
              {candidates.map((d) => (
                <Link to={`/developers/${d.id}`} key={d.id}>
                  <Tag tone="purple">{d.name}</Tag>
                </Link>
              ))}
            </div>
          )}
        </section>
      </div>
    </div>
  )
}
