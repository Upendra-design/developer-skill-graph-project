export default function ErrorState({ message = 'Something went wrong.', onRetry }) {
  return (
    <div className="state-panel error-panel">
      <h3>We hit a snag</h3>
      <p>{message}</p>
      {onRetry && (
        <button className="btn-secondary" onClick={onRetry}>Try again</button>
      )}
    </div>
  )
}
