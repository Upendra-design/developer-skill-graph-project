import { useEffect, useState } from 'react'
import { api } from '../api/client.js'
import LoadingState from '../components/LoadingState.jsx'
import ErrorState from '../components/ErrorState.jsx'
import EmptyState from '../components/EmptyState.jsx'

/**
 * A simple, dependency-free "network view". Rather than pulling in a
 * heavy graph-visualization library, this renders the same relationships
 * as three connected columns (Developer -> Projects -> Technologies),
 * which is enough to clearly demonstrate the graph traversal without
 * adding unnecessary complexity to the project.
 */
export default function GraphExplorer() {
  const [developers, setDevelopers] = useState([])
  const [selectedId, setSelectedId] = useState('')
  const [details, setDetails] = useState(null)
  const [status, setStatus] = useState('loading')
  const [detailStatus, setDetailStatus] = useState('idle')

  useEffect(() => {
    api.getDevelopers()
      .then((res) => {
        setDevelopers(res.data)
        setStatus('ready')
        if (res.data.length > 0) setSelectedId(res.data[0].id)
      })
      .catch(() => setStatus('error'))
  }, [])

  useEffect(() => {
    if (!selectedId) return
    setDetailStatus('loading')
    api.getDeveloperDetails(selectedId)
      .then((res) => {
        setDetails(res.data)
        setDetailStatus('ready')
      })
      .catch(() => setDetailStatus('error'))
  }, [selectedId])

  if (status === 'loading') return <LoadingState label="Loading graph explorer…" />
  if (status === 'error') return <ErrorState message="Could not load developers from CognoDB." />
  if (developers.length === 0) return <EmptyState title="No data to explore" message="Seed data may not be loaded." />

  return (
    <div className="page">
      <div className="page-header">
        <h1>Graph Explorer</h1>
        <p className="page-subtitle">
          Pick a developer to see how they connect outward to their projects and, from
          there, to the technologies those projects use.
        </p>
      </div>

      <div className="explorer-picker">
        <label htmlFor="dev-select">Developer</label>
        <select id="dev-select" value={selectedId} onChange={(e) => setSelectedId(e.target.value)}>
          {developers.map((d) => (
            <option key={d.id} value={d.id}>{d.name}</option>
          ))}
        </select>
      </div>

      {detailStatus === 'loading' && <LoadingState label="Loading connections…" />}
      {detailStatus === 'error' && <ErrorState message="Could not load this developer's connections." />}

      {detailStatus === 'ready' && details && (
        <div className="network-view">
          <div className="network-column">
            <h4>Developer</h4>
            <div className="network-node node-developer">{details.developer.name}</div>
          </div>

          <div className="network-arrow">WORKED_ON →</div>

          <div className="network-column">
            <h4>Projects</h4>
            {details.projects.length === 0 && <p className="muted small">None</p>}
            {details.projects.map((p) => (
              <div className="network-node node-project" key={p.id}>{p.name}</div>
            ))}
          </div>

          <div className="network-arrow">USES →</div>

          <div className="network-column">
            <h4>Connected Technologies</h4>
            {details.connectedTechnologies.length === 0 && <p className="muted small">None</p>}
            {details.connectedTechnologies.map((t) => (
              <div className="network-node node-tech" key={t.id}>{t.name}</div>
            ))}
          </div>
        </div>
      )}
    </div>
  )
}
