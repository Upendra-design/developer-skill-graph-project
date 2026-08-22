import { Link } from 'react-router-dom'

export default function NotFound() {
  return (
    <div className="state-panel empty-panel">
      <h3>Page not found</h3>
      <p>The page you're looking for doesn't exist.</p>
      <Link className="btn-secondary" to="/">Back to dashboard</Link>
    </div>
  )
}
