from dataclasses import dataclass


@dataclass(frozen=True)
class CreateCourseName:
    """
    Represents a course identified by its name.

    Args:
        name: the course name
    """
    name: str
