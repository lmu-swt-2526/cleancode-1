class DatabaseEntryException(Exception):
    """Exception thrown when a database operation fails due to data constraints."""

    def __init__(self, message: str, cause: Exception = None):
        super().__init__(message)
        self.cause = cause
