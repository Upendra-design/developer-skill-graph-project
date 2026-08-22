import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { api } from '../api/client.js'
import LoadingState from '../components/LoadingState.jsx'
import ErrorState from '../components/ErrorState.jsx'
import EmptyState from '../components/EmptyState.jsx'

export default function Projects() {
  const [projects, setProjects] = useState([])
  const [status, setStatus] = useState('loading')

  const load = () => {
    setStatus('loading')
    api.getProjects()
      .then((res) => {
        setProjects(res.data)
        setStatus('ready')
      })
      .catch(() => setStatus('error'))
  }

  useEffect(load, [])

  if (status === 'loading') return <LoadingState label="Loading projects…" />
  if (status === 'error') return <ErrorState message="Could not load projects from CognoDB." onRetry={load} />

  return (
    <div className="page">
      <div className="page-header">
        <h1>Projects</h1>
        <p className="page-subtitle">Explore projects, the technologies they use, and who worked on them.</p>
      </div>

      {projects.length === 0 ? (
        <EmptyState title="No projects yet" message="Seed data may not be loaded." />
      ) : (
        <div className="grid-list">
          {projects.map((p) => (
            <Link to={`/projects/${p.id}`} key={p.id} className="card list-card">
              <h3>{p.name}</h3>
              <p className="muted small">{p.description}</p>
            </Link>
          ))}
        </div>
      )}
    </div>
  )
}
